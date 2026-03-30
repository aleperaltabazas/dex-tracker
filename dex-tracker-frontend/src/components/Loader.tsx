import React from "react";
import { TailSpin } from "react-loader-spinner";

type LoaderProps = {
  color?: string;
};

const defaultColor = "#3B4CCA";

const Loader = (props: LoaderProps) => {
  return (
    <TailSpin
      color={props.color || defaultColor}
      height={100}
      width={100}
    />
  );
};

export default Loader;
