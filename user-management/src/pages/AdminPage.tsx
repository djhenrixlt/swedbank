import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './AdminPage.css';

// Define types for User and component state
interface User {
    id: number;
    name: string;
    email: string;
    active: boolean;
}

const AdminPage: React.FC = () => {
    const [users, setUsers] = useState<User[]>([]); // List of users
    const [selectedUser, setSelectedUser] = useState<User | null>(null); // User being edited
    const [isEditMode, setIsEditMode] = useState<boolean>(false); // Edit mode flag

    // Fetch users when the component is mounted
    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const response = await axios.get<User[]>('/api/v1.0/users', {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('jwtToken')}`,
                    },
                });
                setUsers(response.data); // Set users data
            } catch (error) {
                console.error('Failed to fetch users:', error);
            }
        };
        fetchUsers();
    }, []);

    // Handle edit button click
    const handleEdit = (user: User) => {
        setSelectedUser(user);
        setIsEditMode(true);
    };

    // Handle saving the updated user information
    const handleSave = async () => {
        if (!selectedUser) return; // Check if selectedUser is null
        try {
            await axios.put(`/api/v1.0/users/${selectedUser.id}`, selectedUser, {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem('jwtToken')}`,
                },
            });
            setIsEditMode(false);
            alert('User updated successfully');
        } catch (error) {
            console.error('Failed to update user:', error);
            alert('Failed to update user');
        }
    };

    return (
        <div className="admin-page">
            <h1>Admin Page - Manage Users</h1>

            {/* Users Table */}
            <table className="users-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {users.map((user) => (
                    <tr key={user.id}>
                        <td>{user.id}</td>
                        <td>{user.name}</td>
                        <td>{user.email}</td>
                        <td>{user.active ? 'Active' : 'Inactive'}</td>
                        <td>
                            <button onClick={() => handleEdit(user)}>Edit</button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>

            {/* Edit User Popup */}
            {isEditMode && selectedUser && (
                <div className="edit-user-popup">
                    <h2>Edit User</h2>
                    <label>
                        Name:
                        <input
                            type="text"
                            value={selectedUser.name}
                            onChange={(e) => setSelectedUser({ ...selectedUser, name: e.target.value })}
                        />
                    </label>
                    <label>
                        Email:
                        <input
                            type="email"
                            value={selectedUser.email}
                            onChange={(e) => setSelectedUser({ ...selectedUser, email: e.target.value })}
                        />
                    </label>
                    <label>
                        Status:
                        <select
                            value={selectedUser.active ? "true" : "false"}
                            onChange={(e) => setSelectedUser({ ...selectedUser, active: e.target.value === 'true' })}
                        >
                            <option value="true">Active</option>
                            <option value="false">Inactive</option>
                        </select>
                    </label>
                    <button onClick={handleSave}>Save</button>
                    <button onClick={() => setIsEditMode(false)}>Cancel</button>
                </div>
            )}
        </div>
    );
};

export default AdminPage;
