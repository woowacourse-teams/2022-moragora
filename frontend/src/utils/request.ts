import { Interceptor } from 'types/requestType';

export const interceptor: Interceptor = {
  accessToken: null,
  onRequest: (url, options, accessToken) => ({ url, options }),
  onSuccess: (response) => {},
  onError: (response, body) => {},
  set({ onRequest, onSuccess, onError }) {
    if (onRequest) this.onRequest = onRequest;
    if (onSuccess) this.onSuccess = onSuccess;
    if (onError) this.onError = onError;
  },
};

const request =
  (baseUrl: string) =>
  async <
    SuccessBody,
    ErrorBody extends { message: string } = { message: string }
  >(
    url: string,
    options: RequestInit
  ) => {
    const interceptedRequest = interceptor.onRequest(
      url,
      options,
      interceptor.accessToken
    );
    const res = await fetch(
      `${baseUrl}${interceptedRequest.url}`,
      interceptedRequest.options
    );

    if (!res.ok) {
      const body = (await res.json().catch(() => ({}))) as ErrorBody;

      interceptor.onError(res, body);

      throw new Error(`${res.status}: ${body.message}`);
    }
    const body = (await res.json().catch(() => ({}))) as SuccessBody;

    interceptor.onSuccess(res);

    return { headers: res.headers, status: res.status, body };
  };

export default request(process.env.API_SERVER_HOST ?? '');
