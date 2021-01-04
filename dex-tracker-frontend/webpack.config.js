const fs = require('fs');
const path = require('path');
const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const OptimizeCSSAssetsPlugin = require('optimize-css-assets-webpack-plugin');

const assetsPublicPath = '/';
const staticPath = 'static';
const outputPath = path.join(__dirname, 'build');
const publicPath = path.join(__dirname, 'public');
const mainEntry = path.join(__dirname, 'src', 'entry.tsx');
const environment = {
  isProduction: process.env.NODE_ENV === 'production',
  isDevelopment: process.env.NODE_ENV !== 'production',
};

const file = filename => staticPath.concat(`/${filename}`);
const devtool = environment.isProduction ? 'hidden-source-maps' : 'source-maps';
const mode = environment.isProduction || environment.isBeta
  ? 'production'
  : 'development';

const devPlugins = [
  new webpack.NoEmitOnErrorsPlugin(),
];

const prodPlugins = [
  new OptimizeCSSAssetsPlugin(),
];

const plugins = [
  new webpack.WatchIgnorePlugin([
    // Fixes initial rebuild in dev mode.
    new RegExp(outputPath),
  ]),
  new MiniCssExtractPlugin({
    filename: file('[name].[contenthash].css'),
    chunkFilename: file('[id].[contenthash].css'),
  }),
  new HtmlWebpackPlugin({
    template: path.join(publicPath, 'index.html'),
  }),
  new webpack.DefinePlugin({
    'process.env.NODE_ENV': JSON.stringify(process.env.NODE_ENV || 'development'),
  }),
  ...environment.isProduction ? prodPlugins : devPlugins,
];

const cssLoaders = ({ extract } =
  { extract: false }) => [environment.isProduction || extract ? MiniCssExtractPlugin.loader : 'style-loader']
    .concat({
      loader: 'css-loader',
      options: {
        modules: false,
        camelCase: true,
        importLoaders: true,
        sourceMap: true,
        localIdentName: '[name]__[local]___[hash:base64:5]',
      },
    })
    .concat([
      'resolve-url-loader',
      {
        loader: 'sass-loader',
        options: {
          sourceMap: true,
        },
      },
    ]);

module.exports = {
  mode,
  devtool,
  plugins,
  resolve: {
    extensions: ['.js', '.jsx', '.tsx', '.scss', '.css'],
  },
  entry: {
    main: [
      ...(
        environment.isDevelopment
          ? [
            'react-dev-utils/webpackHotDevClient',
            'webpack/hot/dev-server',
          ]
          : []
      ),
      mainEntry,
    ].filter(Boolean),
    vendor: [
      'react',
      'react-dom',
      'prop-types',
    ],
  },
  output: {
    publicPath: assetsPublicPath,
    path: outputPath,
    filename: environment.isProduction ? file('[name].[contenthash].js') : file('[name].js'),
  },
  module: {
    rules: [
      {
        test: /\.(jsx?)$/,
        exclude: /node_modules/,
        use: {
          loader: 'babel-loader',
        },
      },
      {
        test: /.tsx?$/,
        use: ['awesome-typescript-loader']
      },
      {
        test: /\.js$/,
        exclude: /node_modules/,
        use: ['babel-loader', 'eslint-loader'],
      },
      {
        test: /\.s?css$/,
        use: cssLoaders(),
      },
      {
        test: /\.(jpe?g|png|gif|svg|ttf|woff2?)$/i,
        use: [
          {
            loader: 'file-loader',
            options: {
              publicPath: assetsPublicPath,
              name: file('[name].[hash].[ext]'),
              emitFile: true,
            },
          },
        ],
      },
    ],
  },
  optimization: {
    splitChunks: {
      cacheGroups: {
        vendor: {
          chunks: 'initial',
          name: 'vendor',
          test: 'vendor',
          enforce: true,
        },
      },
    },
  },
};
