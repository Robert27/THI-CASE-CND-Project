export const getMockEnabled = async () => {
  const response = await fetch(
    "http://localhost:4000/rest/interval/mock/enabled"
  );

  return response.json();
};

export const getMockDate = async () => {
  const response = await fetch("http://localhost:4000/rest/interval/mock");

  return response.text();
};

export const setMockDate = async (date: string) => {
  await fetch("http://localhost:4000/rest/interval/mock/date", {
    method: "POST",
    headers: { "Content-Type": "text/plain" },
    body: date,
  });
};
