import React from "react";
import { Alert, AlertTitle, Box } from "@mui/material";

interface AlertProps {
  type?: "error" | "info" | "success" | "warning";
  title?: string;
  message: string;
  onClose?: () => void;
}

const CustomAlert: React.FC<AlertProps> = ({ type = "error", title, message, onClose }) => {
  const defaultTitles = {
    error: "Error",
    info: "Information",
    success: "Success",
    warning: "Warning",
  };

  return (
    <Box sx={{ marginBottom: "1rem", position: "fixed", top: "1rem", right: "1rem" }}>
      <Alert severity={type} onClose={onClose}>
        <AlertTitle>{title || defaultTitles[type]}</AlertTitle>
        {message}
      </Alert>
    </Box>
  );
};

export default CustomAlert;
