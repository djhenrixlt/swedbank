import React, { useState, FormEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import './LoginPage.css';
import { loginUser } from './authService'; // Import the loginUser function from authService

const LoginPage: React.FC = () => {
    const [login, setLogin] = useState<string>(''); // For email or username
    const [password, setPassword] = useState<string>('');
    const [error, setError] = useState<string>(''); // To store error messages
    const [loading, setLoading] = useState<boolean>(false); // To handle loading state
    const navigate = useNavigate(); // Hook to navigate between routes

    const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault(); // Prevent the default form submission behavior
        setLoading(true); // Set loading state to true when form is submitted

        try {
            // Use the loginUser function from authService to send login request
            const response = await loginUser(login, password);

            // If login is successful, store the JWT token
            localStorage.setItem('jwtToken', response.accessToken);

            // Redirect the user to the dashboard page
            navigate('/dashboard');
        } catch (error: any) {
            // Set an error message if the login fails
            setError('Invalid username, email, or password.');
        } finally {
            // Reset the loading state after the request completes
            setLoading(false);
        }
    };

    return (
        <div className="login-page">
            <h1>Login</h1>
            {error && <div className="error">{error}</div>} {/* Display error if any */}
            <form onSubmit={handleSubmit}>
                <label>
                    Username or Email:
                    <input
                        type="text"
                        value={login}
                        onChange={(e) => setLogin(e.target.value)} // Update login state
                        required
                    />
                </label>
                <label>
                    Password:
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)} // Update password state
                        required
                    />
                </label>
                <button type="submit" disabled={loading}>
                    {loading ? 'Logging in...' : 'Login'} {/* Button label changes on loading */}
                </button>
            </form>
        </div>
    );
};

export default LoginPage;
