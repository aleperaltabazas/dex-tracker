import { UserDex } from "../types/user";
import withFetch from "./withFetch";

const withUserDex = (id: string, listener?: string) =>
  withFetch<UserDex>(
    { path: `api/v1/users/pokedex/${id}`, withCredentials: true },
    listener ? [listener] : undefined
  );
export default withUserDex;
