import { Grid, GridProps } from "@material-ui/core";
import React from "react";

type ColumnProps = GridProps;

const Column = (props: ColumnProps) => <Grid item {...props} />;

export default Column;
