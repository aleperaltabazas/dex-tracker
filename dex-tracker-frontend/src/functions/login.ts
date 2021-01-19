import axios, { AxiosRequestConfig } from "axios";
import { host } from "../config";
import { User } from "../types/user";

export function login(token: string) {
  let config: AxiosRequestConfig = {
    method: "GET",
    url: `${host}/api/v1/users`,
    withCredentials: true,
  };

  return axios.request<User>(config);
}

export function createUser() {
  let config: AxiosRequestConfig = {
    url: `${host}/api/v1/users`,
    method: "POST",
    withCredentials: true,
  };

  return axios.request(config);
}
