const BASE = "/hero";

const HttpApiOfHero = {
  CreateRole: `${BASE}/createRole`,
  GetAllRole: `${BASE}/getAllRole`
} as const;

export default HttpApiOfHero;
