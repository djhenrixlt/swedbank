import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const AdminPage = () => {
    const [users, setUsers] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const response = await axios.get('/api/v1.0/users');
                setUsers(response.data);
            } catch (error) {
                alert('Failed to fetch users');
            }
        };

        fetchUsers();
    }, []);

    const handleUpdate = async (user) => {
        // Implement update logic (e.g., navigate to an edit page)
        navigate(`/admin/edit/${user.id}`);
    };

    const handleDelete = async (userId) => {
        try {
            await axios.delete(`/api/v1.0/users/${userId}`);
            setUsers(users.filter(user => user.id !== userId));
            alert('User deleted');
        } catch (error) {
            alert('Deletion failed');
        }
    };

    return (
        <div>
            <h1>Admin Page</h1>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {users.map(user => (
                    <tr key={user.id}>
                        <td>{user.id}</td>
                        <td>{user.name}</td>
                        <td>{user.email}</td>
                        <td>
                            <button onClick={() => handleUpdate(user)}>Edit</button>
                            <button onClick={() => handleDelete(user.id)}>Delete</button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default AdminPage;

