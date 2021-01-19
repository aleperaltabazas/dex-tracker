import { clearSynchronizeQueue } from "../actions/syncQueue";
import store from "../store";
import { Sync } from "../types/sync";

export function synchronize(syncQueue: Sync[]) {
  console.log("sincronizado perro:");
  console.log(syncQueue);
  store.dispatch(clearSynchronizeQueue());
}
