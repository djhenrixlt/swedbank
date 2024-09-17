import axios from 'axios';

export interface AuthResponse {
    accessToken: string;
}

export interface User {
    name: string;
    lastName: string;
    username: string;
    email: string;
    password: string;
}

// Create an axios instance
const api = axios.create({
    baseURL: '/api/v1.0',
    headers: {
        'Content-Type': 'application/json',
    },
});

// Handle login request
export const loginUser = async (login: string, password: string): Promise<AuthResponse> => {
    const response = await api.post<AuthResponse>('/login', { login, password });
    return response.data; // Ensure returning only the data, not the entire Axios response object
};

// Handle user registration
export const registerUser = async (data: User): Promise<void> => {
    await api.post('/signup', data);
};

// Fetch user profile
export const fetchUserProfile = async (token: string | null): Promise<User> => {
    const response = await api.get<User>('/users/self', {
        headers: { Authorization: `Bearer ${token}` },
    });
    return response.data; // Ensure returning only the data
};

// Update user profile
export const updateUserProfile = async (token: string | null, data: User): Promise<void> => {
    await api.post('/users/self', data, {
        headers: { Authorization: `Bearer ${token}` },
    });
};

// Deactivate user account
export const deactivateUserAccount = async (token: string | null): Promise<void> => {
    await api.put('/users/self/deactivate', {}, {
        headers: { Authorization: `Bearer ${token}` },
    });
};
