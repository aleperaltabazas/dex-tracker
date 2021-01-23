import React from "react";
import { hot } from "react-hot-loader";
import ReactLoader from "react-loader-spinner";

type LoaderProps = {
  type?:
    | "Audio"
    | "BallTriangle"
    | "Bars"
    | "Circles"
    | "Grid"
    | "Hearts"
    | "MutatingDots"
    | "None"
    | "NotSpecified"
    | "Oval"
    | "Plane"
    | "Puff"
    | "RevolvingDot"
    | "Rings"
    | "TailSpin"
    | "ThreeDots"
    | "Triangle"
    | "Watch";
  color?: string;
};

const defaultColor = "#3B4CCA";

const Loader = (props: LoaderProps) => {
  return (
    <ReactLoader
      type={props.type || "TailSpin"}
      color={props.color || defaultColor}
      height={100}
      width={100}
      className="center"
    />
  );
};

export default hot(module)(Loader);
