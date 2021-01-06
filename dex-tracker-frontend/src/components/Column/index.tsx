import { Grid, GridProps } from "@material-ui/core";
import React from "react";
import { hot } from "react-hot-loader";

type ColumnProps = GridProps;

const Column = (props: ColumnProps) => <Grid item {...props} />;

export default hot(module)(Column);
