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
      const data = (await res.json()) as ErrorBody;

      throw new Error(`${res.status}: ${data.message}`);
    }

    const data = (await res.json()) as SuccessBody;

    return data;
  };

export default request(process.env.API_SERVER_HOST ?? '');
