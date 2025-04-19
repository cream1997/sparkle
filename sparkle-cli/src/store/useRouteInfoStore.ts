import { defineStore } from "pinia";

const useRouteInfoStore = defineStore("routeInfo", {
  state: () => ({
    heroPageCurrentPath: ""
  })
});

export default useRouteInfoStore;
