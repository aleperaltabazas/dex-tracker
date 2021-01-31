import {
  Box,
  LinearProgressProps,
  Typography,
  LinearProgress as MuiLinearProgress,
} from "@material-ui/core";
import React from "react";
import { hot } from "react-hot-loader";

type LinearProps = LinearProgressProps & { value: number };

const LinearProgress = (props: LinearProps) => {
  return (
    <Box display="flex" alignItems="center">
      <Box width="100%" mr={1}>
        <MuiLinearProgress variant="determinate" {...props} />
      </Box>
      <Box minWidth={35}>
        <Typography variant="body2" color="textSecondary">{`${Math.round(
          props.value
        )}%`}</Typography>
      </Box>
    </Box>
  );
};

export default hot(module)(LinearProgress);
