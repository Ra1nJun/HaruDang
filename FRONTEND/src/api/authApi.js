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
        staleTime: 5 * 60 * 1000, // 5분 동안 신선한 상태 유지
        cacheTime: 10 * 60 * 1000, // 10분 동안 캐시 유지
        retry: false,
        refetchOnMount: false,       // 마운트 시 자동 재요청 금지
        refetchOnWindowFocus: false, // 포커스 복귀 시 재요청 금지
        refetchOnReconnect: false,   // 네트워크 재연결 시 재요청 금지
    });
}

export function useLogout() {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: logout,
        onSuccess: () => {
            // 로그아웃 시 아예 캐시 삭제
            queryClient.removeQueries({ queryKey: [AUTH_QUERY_KEY] });
        },
    });
}