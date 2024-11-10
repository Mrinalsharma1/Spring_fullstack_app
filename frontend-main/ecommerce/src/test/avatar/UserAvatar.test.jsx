import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import { BrowserRouter as Router } from 'react-router-dom';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import UserAvatar from '../../components/avatar/UserAvatar';
import userReducer, { logout } from '../../redux/userSlice';

const mockStore = configureStore({
    reducer: {
        user: (state = { user: 'John' }, action) => state,
    },
});

describe('UserAvatar Component', () => {
    let store;

    beforeEach(() => {
        store = mockStore;
    });

    test('renders user initial correctly', () => {
        render(
            <Provider store={store}>
                <Router>
                    <UserAvatar user="John" />
                </Router>
            </Provider>
        );
        const avatarElement = screen.getByText('J');
        expect(avatarElement).toBeInTheDocument();
    });

    test('dropdown menu opens on avatar click', () => {
        render(
            <Provider store={store}>
                <Router>
                    <UserAvatar user="John" />
                </Router>
            </Provider>
        );
        const avatarElement = screen.getByText('J');
        fireEvent.click(avatarElement);
        const profileLink = screen.getByText('My Profile');
        expect(profileLink).toBeInTheDocument();
    });

    test('navigates to profile page on clicking My Profile', () => {
        render(
            <Provider store={store}>
                <Router>
                    <UserAvatar user="John" />
                </Router>
            </Provider>
        );
        const avatarElement = screen.getByText('J');
        fireEvent.click(avatarElement);
        const profileLink = screen.getByText('My Profile');
        fireEvent.click(profileLink);
        expect(window.location.pathname).toBe('/profile');
    });

    test('navigates to bookings page on clicking My Booking', () => {
        render(
            <Provider store={store}>
                <Router>
                    <UserAvatar user="John" />
                </Router>
            </Provider>
        );
        const avatarElement = screen.getByText('J');
        fireEvent.click(avatarElement);
        const bookingLink = screen.getByText('My Booking');
        fireEvent.click(bookingLink);
        expect(window.location.pathname).toBe('/bookings');
    });

    test('dispatches logout action and navigates to home on clicking Logout', () => {
        store.dispatch = jest.fn();
        delete window.location;
        window.location = { pathname: '' }; // Mocking window.location

        render(
            <Provider store={store}>
                <Router>
                    <UserAvatar user="John" />
                </Router>
            </Provider>
        );
        const avatarElement = screen.getByText('J');
        fireEvent.click(avatarElement);
        const logoutLink = screen.getByText('Logout');
        fireEvent.click(logoutLink);
        expect(store.dispatch).toHaveBeenCalledWith(logout());
        window.location.pathname = '/';
        expect(window.location.pathname).toBe('/');
    });

    test('StyledBadge renders correctly', () => {
        render(
            <Provider store={store}>
                <Router>
                    <UserAvatar user="John" />
                </Router>
            </Provider>
        );
        
    });
});
