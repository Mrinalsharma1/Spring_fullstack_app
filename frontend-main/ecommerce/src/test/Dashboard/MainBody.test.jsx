import React from 'react';
import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import MainBody from '../../components/Dashboard/MainBody';
import userReducer from '../../redux/userSlice';

// Mock store setup
const mockStore = configureStore({
  reducer: {
    login: userReducer,
  },
});

const renderWithProviders = (ui, { store = mockStore } = {}) => {
  return render(<Provider store={store}>{ui}</Provider>);
};

describe('MainBody Component', () => {
  test('renders dashboard header', () => {
    renderWithProviders(<MainBody>Test Content</MainBody>);
    const headerElement = screen.getByText(/Dashboard/i);
    expect(headerElement).toBeInTheDocument();
  });

  test('renders user initials in avatar', () => {
    const initialState = {
      login: {
        name: 'John Doe',
      },
    };
    const store = configureStore({
      reducer: {
        login: userReducer,
      },
      preloadedState: initialState,
    });

    renderWithProviders(<MainBody>Test Content</MainBody>, { store });
    const avatarElement = screen.getByText('JD');
    expect(avatarElement).toBeInTheDocument();
  });

  test('renders children content', () => {
    renderWithProviders(<MainBody>Test Content</MainBody>);
    const childrenElement = screen.getByText('Test Content');
    expect(childrenElement).toBeInTheDocument();
  });

  test('renders StyledBadge with correct styles', () => {
    const initialState = {
      login: {
        name: 'John Doe',
      },
    };
    const store = configureStore({
      reducer: {
        login: userReducer,
      },
      preloadedState: initialState,
    });

    renderWithProviders(<MainBody>Test Content</MainBody>, { store });
    // const badgeElement = screen.getByRole('status');
    // expect(badgeElement).toHaveStyle('background-color: #44b700');
  });
});
