import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useForm, SubmitHandler } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';

// Define types for user data and form input
interface User {
    name: string;
    lastName: string;
    username: string;
    email: string;
}

interface FormValues extends User {}

const UserProfilePage: React.FC = () => {
    const [user, setUser] = useState<User | null>(null);
    const { register, handleSubmit, reset } = useForm<FormValues>();
    const navigate = useNavigate();

    useEffect(() => {
        // Fetch user data from API
        const fetchUser = async () => {
            const token = localStorage.getItem('jwtToken'); // Use stored JWT token
            try {
                const response = await axios.get<User>(`/api/v1.0/users/self`, {
                    headers: { 'Authorization': `Bearer ${token}` } // Pass token for auth
                });
                setUser(response.data);
                reset(response.data);
            } catch (error) {
                alert('Failed to fetch user data');
                console.error(error);
            }
        };

        fetchUser();
    }, [reset]);

    const onSubmit: SubmitHandler<FormValues> = async (data) => {
        try {
            const token = localStorage.getItem('jwtToken');
            await axios.post(`/api/v1.0/users/self`, data, {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            alert('Profile updated successfully');
        } catch (error) {
            alert('Failed to update profile');
            console.error(error);
        }
    };

    const handleDeactivate = async () => {
        if (window.confirm("Are you sure you want to deactivate your account?")) {
            try {
                const token = localStorage.getItem('jwtToken');
                await axios.put(`/api/v1.0/users/self/deactivate`, {}, {
                    headers: { 'Authorization': `Bearer ${token}` }
                });
                localStorage.removeItem('jwtToken'); // Remove JWT token
                navigate('/login');
                alert('Account deactivated');
            } catch (error) {
                alert('Failed to deactivate account');
                console.error(error);
            }
        }
    };

    if (!user) return <p>Loading...</p>;

    return (
        <div>
            <h1>Your Profile</h1>
            <form onSubmit={handleSubmit(onSubmit)}>
                <div>
                    <label>Name:</label>
                    <input {...register('name')} placeholder="Name" />
                </div>
                <div>
                    <label>Last Name:</label>
                    <input {...register('lastName')} placeholder="Last Name" />
                </div>
                <div>
                    <label>Username:</label>
                    <input {...register('username')} placeholder="Username" />
                </div>
                <div>
                    <label>Email:</label>
                    <input {...register('email')} placeholder="Email" />
                </div>
                <button type="submit">Update Profile</button>
            </form>
            <button onClick={handleDeactivate} style={{ marginTop: '20px', color: 'red' }}>
                Deactivate Account
            </button>
        </div>
    );
};

export default UserProfilePage;
