import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './LoginPage.css'; // Optional: CSS file for styling

const LoginPage = () => {
    const { register, handleSubmit, formState: { errors } } = useForm();
    const [loading, setLoading] = useState(false); // State to track loading
    const navigate = useNavigate();

    // Handle form submission and login request
    const onSubmit = async (data) => {
        setLoading(true); // Show loading indicator
        try {
            // Make a POST request to the Spring Boot backend for authentication
            const response = await axios.post('/api/v1.0/login', {
                emailOrNickName: data.emailOrNickName,
                password: data.password
            }, {
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            // Extract JWT token from the correct key (accessToken)
            const jwtToken = response.data.accessToken;

            // Save the token in localStorage
            localStorage.setItem('jwtToken', jwtToken);

            // Redirect to the dashboard after successful login
            navigate('/dashboard');
        } catch (error) {
            // Handle error response and display an appropriate message
            if (error.response) {
                console.error('Login failed:', error.response.data);
                alert(`Login failed: ${error.response.data.message || 'Invalid credentials'}`);
            } else {
                console.error('An unexpected error occurred:', error);
                alert('An unexpected error occurred. Please try again.');
            }
        } finally {
            setLoading(false); // Hide loading indicator
        }
    };

    return (
        <div className="login-container">
            <h1>Login</h1>
            <form onSubmit={handleSubmit(onSubmit)} className="login-form">
                <div className="form-group">
                    <input
                        {...register('emailOrNickName', { required: 'Email or Nickname is required' })}
                        placeholder="Email or Nickname"
                        className="form-input"
                    />
                    {errors.emailOrNickName && <p className="error-text">{errors.emailOrNickName.message}</p>}
                </div>
                <div className="form-group">
                    <input
                        {...register('password', { required: 'Password is required' })}
                        type="password"
                        placeholder="Password"
                        className="form-input"
                    />
                    {errors.password && <p className="error-text">{errors.password.message}</p>}
                </div>
                <button type="submit" className="login-button" disabled={loading}>
                    {loading ? 'Logging in...' : 'Login'}
                </button>
            </form>
        </div>
    );
};

export default LoginPage;
