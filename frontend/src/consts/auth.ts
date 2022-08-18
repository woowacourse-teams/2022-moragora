const GOOGLE_AUTH_BASE_URL = 'https://accounts.google.com/o/oauth2/v2/auth';

const GOOGLE_AUTH_SCOPE =
  'https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile';

const params = new URLSearchParams({
  client_id: process.env.CLIENT_ID ?? '',
  redirect_uri: `${location.origin}/login`,
  response_type: 'code',
  scope: GOOGLE_AUTH_SCOPE,
});

export const GOOGLE_AUTH_URI = GOOGLE_AUTH_BASE_URL + '?' + params.toString();
