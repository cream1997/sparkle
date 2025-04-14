const BASE = "/hero";

const HttpApiOfHero = {
  Register: `${BASE}/register`,
  Login: `${BASE}/login`
} as const;

export default HttpApiOfHero;
