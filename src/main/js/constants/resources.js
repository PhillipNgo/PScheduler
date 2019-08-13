const api = (process.env.NODE_ENV === 'production') ? 'https://fdnvrxrba7.execute-api.us-east-1.amazonaws.com/beta' : `http://localhost:${devPorts.backend}/api`;

export const courseSearchUrl = `${api}/courses/search`;

export const gpaSearchUrl = `${api}/gpas/search`;

export const shortenUrl = `${api}/shorten`;

export const retrieveShortUrl = 'https://s3.amazonaws.com/pscheduler-urlshortening';
