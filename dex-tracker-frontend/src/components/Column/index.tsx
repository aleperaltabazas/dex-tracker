import { Grid } from "@mui/material";
import type { GridSize } from "@mui/material/Grid";
import React from "react";

type ColumnProps = Omit<React.ComponentProps<typeof Grid>, "size"> & {
  xs?: GridSize | null;
  sm?: GridSize | null;
  md?: GridSize | null;
  lg?: GridSize | null;
  xl?: GridSize | null;
};

const Column = ({ xs, sm, md, lg, xl, ...props }: ColumnProps) => (
  <Grid size={{ xs, sm, md, lg, xl }} {...props} />
);

export default Column;
