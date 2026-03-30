import { Grid, GridProps } from "@mui/material";
import React from "react";

type RowProps = GridProps;

const Row = (props: RowProps) => <Grid container {...props} />;

export default Row;
