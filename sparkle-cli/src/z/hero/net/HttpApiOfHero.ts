const BASE = "/hero";

const HttpApiOfHero = {
  Register: `${BASE}/register`,
  Login: `${BASE}/login`,
  CreateRole: `${BASE}/createRole`
} as const;

export default HttpApiOfHero;
