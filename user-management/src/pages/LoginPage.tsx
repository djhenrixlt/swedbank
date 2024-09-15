import React, { useState, FormEvent } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom'; // Import useNavigate hook
import './LoginPage.css';

// Define types for component state
interface AuthResponse {
    accessToken: string;
}

const LoginPage: React.FC = () => {
    const [login, setLogin] = useState<string>(''); // This will hold either email or username
    const [password, setPassword] = useState<string>('');
    const [error, setError] = useState<string>('');
    const navigate = useNavigate(); // Initialize useNavigate hook

    const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        try {
            // Adjust request payload to send login (username or email) and password
            const response = await axios.post<AuthResponse>('/api/v1.0/login', { login, password });

            // Store the token in localStorage
            localStorage.setItem('jwtToken', response.data.accessToken);

            // Redirect to the dashboard page upon successful login
            navigate('/dashboard');
        } catch (error) {
            setError('Invalid username or email, or password'); // Adjusted error message
        }
    };

    return (
        <div className="login-page">
            <h1>Login</h1>
            {error && <div className="error">{error}</div>}
            <form onSubmit={handleSubmit}>
                <label>
                    Username or Email:
                    <input
                        type="text" // Changed to text to accept both username and email
                        value={login}
                        onChange={(e) => setLogin(e.target.value)}
                        required
                    />
                </label>
                <label>
                    Password:
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </label>
                <button type="submit">Login</button>
            </form>
        </div>
    );
};

export default LoginPage;
