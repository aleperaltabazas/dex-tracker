import { UserDex } from "../types/user";
import withFetch from "./withFetch";

const withUserDex = (userId: string, dexId: string, listener?: Array<string>) =>
  withFetch<UserDex>(
    { path: `api/v1/users/${userId}/pokedex/${dexId}`, withCredentials: true },
    listener
  );
export default withUserDex;
