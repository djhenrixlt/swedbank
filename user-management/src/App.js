import React from 'react';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import UserProfilePage from './pages/UserProfilePage';
import DashboardPage from './pages/DashboardPage';
import AdminPage from './pages/AdminPage';
import PrivateRoute from './components/PrivateRoute';
import AdminRoute from './components/AdminRoute';
import CurrencyExchangePage from './pages/CurrencyExchangePage';
import './App.css';

function App() {
    return (
        <Router>
            <div className="app-container">
                <header className="app-header">
                    <nav>
                        <ul className="nav-menu">
                            <li>
                                <Link to="/">Home</Link>
                            </li>
                            <li>
                                <Link to="/login">Login</Link>
                            </li>
                            <li>
                                <Link to="/register">Register</Link>
                            </li>
                            <li>
                                <Link to="/profile">Profile</Link>
                            </li>
                            <li>
                                <Link to="/dashboard">Dashboard</Link>
                            </li>
                            <li>
                                <Link to="/admin">Admin</Link>
                            </li>
                        </ul>
                    </nav>
                </header>

                <main className="app-main">
                    <Routes>
                        <Route path="/login" element={<LoginPage />} />
                        <Route path="/register" element={<RegisterPage />} />
                        <Route path="/profile" element={<PrivateRoute component={UserProfilePage} />} />
                        <Route path="/dashboard" element={<DashboardPage />} />
                        <Route path="/currency-exchange" element={<CurrencyExchangePage />} />
                        <Route path="/admin" element={<AdminRoute component={AdminPage} />} />
                        <Route path="/" element={<HomePage />} />
                    </Routes>
                </main>

                <footer className="app-footer">
                    <p>&copy; 2024 User Management. All rights reserved.</p>
                </footer>
            </div>
        </Router>
    );
}

const HomePage = () => (
    <div className="home-page">
        <h1>Welcome to User Management</h1>
        <p>Manage your profile and admin settings easily.</p>
    </div>
);

export default App;
