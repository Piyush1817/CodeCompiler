function RunButton(props) {

    return (
        <button
            onClick={props.onRun}
            disabled={props.loading}
        >
            {
                props.loading
                    ? "Running..."
                    : "Run"
            }
        </button>
    );

}

export default RunButton;