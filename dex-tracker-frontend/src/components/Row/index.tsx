import { Grid, GridProps } from "@material-ui/core";
import React from "react";

type RowProps = GridProps;

const Row = (props: RowProps) => <Grid container {...props} />;

export default Row;
