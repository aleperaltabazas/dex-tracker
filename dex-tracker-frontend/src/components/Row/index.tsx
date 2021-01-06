import { Grid, GridProps } from "@material-ui/core";
import React from "react";
import { hot } from "react-hot-loader";

type RowProps = GridProps;

const Row = (props: RowProps) => <Grid container {...props} />;

export default hot(module)(Row);
