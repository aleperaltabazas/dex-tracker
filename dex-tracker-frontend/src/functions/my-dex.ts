import { clearSynchronizeQueue } from "../actions/syncQueue";
import store from "../store";
import { Sync } from "../types/sync";
import axios, { AxiosRequestConfig } from "axios";
import { host } from "../config";

export function createUser() {
  let config: AxiosRequestConfig = {
    url: `${host}/api/v1/users`,
    method: "POST",
    withCredentials: true,
  };

  return axios.request(config);
}

export function synchronize(syncQueue: Sync[]) {
  console.log("sincronizado perro:");
  console.log(syncQueue);
  store.dispatch(clearSynchronizeQueue());
}
