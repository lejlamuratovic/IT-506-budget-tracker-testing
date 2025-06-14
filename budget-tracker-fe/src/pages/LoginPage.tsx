import React, { useEffect, useState } from 'react';
import {
  Box,
  TextField,
  Button,
  Paper,
  Typography,
  CircularProgress,
} from '@mui/material';
import { useLogin } from '../hooks';
import { useNavigate } from 'react-router-dom';

const LoginPage: React.FC = () => {
  const [email, setEmail] = useState('');
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();
  const loginMutation = useLogin();

  // Redirect to dashboard if user is already logged in
  useEffect(() => {
    const userId = localStorage.getItem('userId');
    const userEmail = localStorage.getItem('userEmail');
    
    if (userId && userEmail) {
      navigate('/dashboard');
    }
  }, [navigate]);

  const handleLogin = async () => {
    setError(null);
    try {
      const user = await loginMutation.mutateAsync(email);
      // Save user details in local storage or context
      localStorage.setItem('userId', user.id.toString());
      localStorage.setItem('userEmail', user.email);

      // Redirect to the dashboard
      navigate('/dashboard');
    } catch {
      setError('Failed to log in. Please try again.');
    }
  };

  return (
    <Box
      display="flex"
      justifyContent="center"
      alignItems="center"
      minHeight="100vh"
    >
      <Paper sx={{ padding: '2rem', maxWidth: '400px', width: '100%' }}>
        <Typography variant="h4" gutterBottom textAlign="center">
          Login
        </Typography>

        {error && (
          <Typography color="error" textAlign="center" marginBottom="1rem">
            {error}
          </Typography>
        )}

        <TextField
          label="Email"
          type="email"
          fullWidth
          size="small"
          margin="normal"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          disabled={loginMutation.status === 'pending'}
        />

        <Button
          variant="contained"
          color="primary"
          fullWidth
          onClick={handleLogin}
          disabled={!email || loginMutation.status === 'pending'}
          sx={{ marginTop: '1rem' }}
        >
          {loginMutation.status === 'pending' ? (
            <CircularProgress size={24} color="inherit" />
          ) : (
            'Log In'
          )}
        </Button>
      </Paper>
    </Box>
  );
};

export default LoginPage;
