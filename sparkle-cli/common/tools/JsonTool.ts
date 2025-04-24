export function JsonSafeParse(str: string): object {
  return JSON.parse(str, (key, value: any | LongObj) => {
    if (value._isLong) {
      return BigInt(value._longStr);
    }
    return value;
  });
}

export function JsonSafeStringify(obj: any) {
  return JSON.stringify(obj, (_key, value) => {
    if (typeof value === "bigint") {
      return {
        _isLong: true,
        _longStr: value.toString()
      } as LongObj;
    } else {
      return value;
    }
  });
}

type LongObj = {
  _isLong: true;
  _longStr: string;
};
type MayBeLongObj = LongObj | { _isLong: undefined; _longStr: undefined };

function convertObj(obj: MayBeLongObj): object | bigint {
  if (obj._isLong) {
    return convertLongStr2BigInt(obj._longStr);
  } else {
    return obj;
  }
}

function convertLongStr2BigInt(longStr: string): bigint {
  if (!/^[-+]?\d+$/.test(longStr)) {
    throw new Error(`Invalid Long String: ${longStr}`);
  } else {
    // 是数字
    return BigInt(longStr);
  }
}
