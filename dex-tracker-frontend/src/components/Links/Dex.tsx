import React, { ReactNode } from "react";
import { hot } from "react-hot-loader";
import { Link } from "react-router-dom";

type DexLinkProps = {
  dexId: string;
  children: ReactNode;
};

const DexLink = (props: DexLinkProps) => (
  <Link className="normalize-link" to={`/dex/${props.dexId}`}>
    {props.children}
  </Link>
);

export default hot(module)(DexLink);
