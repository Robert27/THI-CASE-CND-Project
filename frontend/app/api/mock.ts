import { httpHost } from "../providers";

export const getMockEnabled = async () => {
  const response = await fetch(httpHost + "/rest/interval/mock/enabled");

  return response.json();
};

export const getMockDate = async () => {
  const response = await fetch(httpHost + "/rest/interval/mock");

  return response.text();
};

export const setMockDate = async (date: string) => {
  await fetch(httpHost + "/rest/interval/mock/date", {
    method: "POST",
    headers: { "Content-Type": "text/plain" },
    body: date,
  });
};
