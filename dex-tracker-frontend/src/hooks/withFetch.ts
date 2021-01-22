import axios, { AxiosRequestConfig } from "axios";
import { useEffect, useState } from "react";
import { host } from "../config";

export const PENDING = "PENDING";
export const SUCCESS = "SUCCESS";
export const ERROR = "ERROR";

interface FetchSuccess<T> {
  value: T;
  type: typeof SUCCESS;
}

interface FetchError {
  type: typeof ERROR;
  status?: number;
}

interface FetchPending {
  type: typeof PENDING;
}

export type FetchStatus<T> = FetchSuccess<T> | FetchError | FetchPending;

export type FetchConfig = {
  path: string;
  withCredentials?: boolean;
};

export default function withFetch<T>(
  config: FetchConfig,
  updateListener?: Array<string>
): [FetchStatus<T>, (path: string) => void] {
  const [status, setStatus] = useState<FetchStatus<T>>({ type: PENDING });
  const [isFetching, setIsFetching] = useState(false);

  const initialPath = config.path;
  const withCredentials = config.withCredentials || false;

  const fetch = (path?: string) => {
    if (!isFetching) {
      setIsFetching(true);

      setStatus({ type: PENDING });
      let config: AxiosRequestConfig = {
        url: `${host}/${path || initialPath}`,
        method: "GET",
        withCredentials: withCredentials,
      };

      axios
        .request<T>(config)
        .then((res) => res.data)
        .then((t) => setStatus({ type: SUCCESS, value: t }))
        .catch((e) => setStatus({ type: ERROR, status: e.response?.status }))
        .then(() => setIsFetching(false));
    }
  };

  useEffect(fetch, updateListener || []);

  return [status, fetch];
}
