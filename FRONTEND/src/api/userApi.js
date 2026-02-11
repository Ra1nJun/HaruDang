import httpClient from './httpClient';

export function register(nickname, email, password) {
    return httpClient.post('/users/register', { nickname, email, password});
}