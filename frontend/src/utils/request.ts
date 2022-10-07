import { Interceptor } from 'types/requestType';

export const interceptor: Interceptor = {
  onSuccess: (response) => {},
  onError: (response) => {},
  set: function (onSuccess, onError) {
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
    const res = await fetch(`${baseUrl}${url}`, options);

    if (!res.ok) {
      const body = (await res.json().catch(() => ({}))) as ErrorBody;

      interceptor.onError(res);

      throw new Error(`${res.status}: ${body.message}`);
    }

    interceptor.onSuccess(res);

    const body = (await res.json().catch(() => ({}))) as SuccessBody;

    return { headers: res.headers, status: res.status, body };
  };

export default request(process.env.API_SERVER_HOST ?? '');
