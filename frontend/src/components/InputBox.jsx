function InputBox(props) {

    return (
        <textarea
            rows="6"
            placeholder="Enter custom input..."
            value={props.input}
            onChange={(e) =>
                props.setInput(
                    e.target.value
                )
            }
        />
    );
}

export default InputBox;