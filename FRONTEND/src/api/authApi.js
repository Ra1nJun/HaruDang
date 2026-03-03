import httpClient from './httpClient';
import { useQuery, useQueryClient, useMutation } from '@tanstack/react-query';

export function login(email, password) {
    return httpClient.post('/auth/login', { email, password });
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

// React Query hooks
export const AUTH_QUERY_KEY = 'auth';

export function useMe() {
    return useQuery({
        queryKey: [AUTH_QUERY_KEY],
        queryFn: () => me().then(res => res.data),
        staleTime: 5 * 60 * 1000, // 5분 캐시
        gcTime: 10 * 60 * 1000, // 10분 동안 메모리 유지
        retry: false,
    });
}

export function useLogout() {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: logout,
        onSuccess: () => {
            // 캐시 무효화
            queryClient.invalidateQueries({ queryKey: [AUTH_QUERY_KEY] });
        },
    });
}