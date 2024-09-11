import React from 'react';
import { useForm } from 'react-hook-form';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const LoginPage = () => {
    const { register, handleSubmit, formState: { errors } } = useForm();
    const navigate = useNavigate();

    // Handle form submission and login request
    const onSubmit = async (data) => {
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

            // Assuming the response contains the JWT token (adjust according to your backend response structure)
            const jwtToken = response.data.token;

            // Save the token in localStorage or sessionStorage
            localStorage.setItem('jwtToken', jwtToken);

            // Redirect the user to a protected page after successful login
            navigate('/dashboard');
        } catch (error) {
            // Handle error response and display an appropriate message
            if (error.response) {
                console.error('Login failed:', error.response.data);
                alert(`Login failed: ${error.response.data.message || 'Unknown error'}`);
            } else {
                console.error('An unexpected error occurred:', error);
                alert('An unexpected error occurred. Please try again.');
            }
        }
    };

    return (
        <div>
            <h1>Login</h1>
            <form onSubmit={handleSubmit(onSubmit)}>
                <div>
                    <input
                        {...register('emailOrNickName', { required: 'Email or Nickname is required' })}
                        placeholder="Email or Nickname"
                    />
                    {errors.emailOrNickName && <p style={{ color: 'red' }}>{errors.emailOrNickName.message}</p>}
                </div>
                <div>
                    <input
                        {...register('password', { required: 'Password is required' })}
                        type="password"
                        placeholder="Password"
                    />
                    {errors.password && <p style={{ color: 'red' }}>{errors.password.message}</p>}
                </div>
                <button type="submit">Login</button>
            </form>
        </div>
    );
};

export default LoginPage;
