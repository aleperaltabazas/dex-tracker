import React, { ReactNode } from "react";
import { hot } from "react-hot-loader";
import { Link } from "react-router-dom";

type HomeLinkProps = {
  children: ReactNode;
};

const HomeLink = (props: HomeLinkProps) => <Link to="/">{props.children}</Link>;

export default hot(module)(HomeLink);
