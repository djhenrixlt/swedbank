import React from 'react';
import { useForm, SubmitHandler } from 'react-hook-form';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './RegisterPage.css'; // Assuming this CSS file is present

interface RegisterFormData {
    name: string;
    lastName: string;
    username: string;
    email: string;
    password: string;
}

const RegisterPage: React.FC = () => {
    const { register, handleSubmit, formState: { errors } } = useForm<RegisterFormData>();
    const navigate = useNavigate();

    const onSubmit: SubmitHandler<RegisterFormData> = async (data) => {
        try {
            const response = await axios.post('/api/v1.0/signup', data, {
                headers: {
                    'Content-Type': 'application/json',
                },
            });
            if (response.status === 201) {
                navigate('/login'); // Redirect to login page after successful registration
            }
        } catch (error) {
            if (axios.isAxiosError(error)) {
                // TypeScript can recognize AxiosError
                console.error("Backend responded with status", error.response?.status, ":", error.response?.data);
                alert(`Registration failed: ${error.response?.data || error.message}`);
            } else {
                console.error("Unexpected error:", error);
                alert(`Registration failed: ${error instanceof Error ? error.message : 'Unknown error'}`);
            }
        }
    };

    return (
        <div className="register-container">
            <h1>Register</h1>
            <form onSubmit={handleSubmit(onSubmit)} className="register-form">
                <div className="form-group">
                    <label>Name</label>
                    <input {...register('name', { required: 'Name is required' })} placeholder="Name" />
                    {errors.name && <p className="error-text">{errors.name.message}</p>}
                </div>

                <div className="form-group">
                    <label>Last Name</label>
                    <input {...register('lastName', { required: 'Last Name is required' })} placeholder="Last Name" />
                    {errors.lastName && <p className="error-text">{errors.lastName.message}</p>}
                </div>

                <div className="form-group">
                    <label>Username</label>
                    <input {...register('username', { required: 'Username is required' })} placeholder="Username" />
                    {errors.username && <p className="error-text">{errors.username.message}</p>}
                </div>

                <div className="form-group">
                    <label>Email</label>
                    <input {...register('email', { required: 'Email is required', pattern: { value: /^[^\s@]+@[^\s@]+\.[^\s@]+$/, message: 'Invalid email address' } })} placeholder="Email" type="email" />
                    {errors.email && <p className="error-text">{errors.email.message}</p>}
                </div>

                <div className="form-group">
                    <label>Password</label>
                    <input {...register('password', { required: 'Password is required' })} type="password" placeholder="Password" />
                    {errors.password && <p className="error-text">{errors.password.message}</p>}
                </div>

                <button type="submit" className="register-button">Register</button>
            </form>
        </div>
    );
};

export default RegisterPage;
