import React, { ReactNode } from "react";
import { Link } from "react-router-dom";

type DexLinkProps = {
  dexId: string;
  userId: string;
  children: ReactNode;
};

const DexLink = (props: DexLinkProps) => (
  <Link
    className="normalize-link"
    to={`/users/${props.userId}/dex/${props.dexId}`}
  >
    {props.children}
  </Link>
);

export default DexLink;
