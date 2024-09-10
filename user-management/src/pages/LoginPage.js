import React from 'react';
import { useForm } from 'react-hook-form';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const LoginPage = () => {
    const { register, handleSubmit } = useForm();
    const navigate = useNavigate();

    const onSubmit = async (data) => {
        try {
            const response = await axios.post('/api/v1.0/login', {
                nickName: data.nickName,
                password: data.password
            }, {
                headers: {
                    'Content-Type': 'application/json',
                },
            });
            console.log('Login successful:', response.data);
            // Proceed with the response (e.g., redirect or save auth token)
        } catch (error) {
            if (error.response) {
                console.error('Login failed:', error.response.data);
                alert(`Login failed: ${error.response.data}`);
            } else {
                console.error('An unexpected error occurred:', error);
            }
        }
    };

    return (
        <form onSubmit={handleSubmit(onSubmit)}>
            <input {...register('emailOrNickName')} placeholder="Email or Nickname" />
            <input {...register('password')} type="password" placeholder="Password" />
            <button type="submit">Login</button>
        </form>
    );
};

export default LoginPage;
