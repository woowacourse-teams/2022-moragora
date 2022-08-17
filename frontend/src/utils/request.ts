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
      const body = (await res.json()) as ErrorBody;

      throw new Error(`${res.status}: ${body.message}`);
    }

    const body = (await res.json().catch(() => ({}))) as SuccessBody;

    return { headers: res.headers, status: res.status, body };
  };

export default request(process.env.API_SERVER_HOST ?? '');
