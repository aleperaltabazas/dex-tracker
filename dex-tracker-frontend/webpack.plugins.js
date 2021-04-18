const webpack = require("webpack");
const path = require("path");
const CopyWebpackPlugin = require("copy-webpack-plugin");
const OptimizeCssAssetsPlugin = require("optimize-css-assets-webpack-plugin");
const UglifyJSPlugin = require("uglifyjs-webpack-plugin");
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const { CleanWebpackPlugin } = require("clean-webpack-plugin");
const BundleAnalyzerPlugin = require("webpack-bundle-analyzer")
  .BundleAnalyzerPlugin;
const Utils = require("./webpack.utils");

const version = Utils.getPomVersion();
const pathsToClean = [
  path.join(__dirname, "mytrips", "statics", "versions", version, "build"),
];

const base = [
  new webpack.DefinePlugin({
    VERSION: JSON.stringify(version),
  }),
  new CopyWebpackPlugin([
    { from: "images", to: "images" },
    { from: "eva", to: "eva" },
    { from: "stylesheets", to: "stylesheets" },
  ]),
];

const development = [
  new BundleAnalyzerPlugin({
    analyzerHost: "127.0.0.1",
    analyzerPort: 3333,
    openAnalyzer: false,
  }),
  new webpack.HotModuleReplacementPlugin(),
  new CleanWebpackPlugin({
    cleanOnceBeforeBuildPatterns: pathsToClean,
    cleanAfterEveryBuildPatterns: ["!*"],
    dangerouslyAllowCleanPatternsOutsideProject: true,
  }),
];

const production = [
  new OptimizeCssAssetsPlugin({}),
  new UglifyJSPlugin({
    sourceMap: true,
  }),
  new MiniCssExtractPlugin({
    filename: path.join("build.css"),
  }),
];

module.exports = {
  base,
  development,
  production,
};
