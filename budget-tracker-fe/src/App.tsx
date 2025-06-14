import { Routes, Route } from 'react-router-dom';

import { LoginPage, DashboardPage } from './pages';

const App: React.FC = () => {
  return (
    <Routes>
      <Route path="/" element={<LoginPage />} />
      <Route path="/dashboard" element={<DashboardPage />} />
    </Routes>
  );
};

export default App;
