import moment from "moment";

class Times {
  public static now(): number {
    return Date.now();
  }

  public static format(timestamp: number) {
    return moment(timestamp).format("MM-DD HH:mm:ss");
  }
}

export default Times;
