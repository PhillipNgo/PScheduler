const api = 'https://fdnvrxrba7.execute-api.us-east-1.amazonaws.com/beta';

export const courseSearchUrl = `${process.env.NODE_ENV === 'production' ? api : `http://localhost:${devPorts.backend}/api`}/courses/search`;

export const shortenUrl = `${api}/shorten`;

export const retrieveShortUrl = 'https://s3.amazonaws.com/pscheduler-urlshortening';
