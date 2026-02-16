import httpClient from './httpClient';
import qs from 'qs'; 

export function login(email, password) {
    return httpClient.post('/auth/login', { email, password }, {
    withCredentials: true
    });
}

export function reissue() {
    return httpClient.post('/auth/reissue');
}

export function logout() {
    return httpClient.post('/auth/logout');
}

export function me() {
    return httpClient.get('/auth/me');
}