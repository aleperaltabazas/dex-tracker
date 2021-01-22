import { UserDex } from "../types/user";
import withFetch from "./withFetch";

const withUserDex = (listener?: string) =>
  withFetch<UserDex>(
    { path: "api/v1/users", withCredentials: true },
    listener ? [listener] : undefined
  );
export default withUserDex;
